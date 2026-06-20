provider "aws" {
  region = var.aws_region
}

resource "aws_db_instance" "postgres" {
  identifier           = "franquicias-db"
  engine               = "postgres"
  engine_version       = "16.3"
  instance_class       = "db.t3.micro"
  allocated_storage    = 20
  db_name              = "franquicias"
  username             = var.db_user
  password             = var.db_password
  skip_final_snapshot  = true
  publicly_accessible  = false

  tags = {
    Name = "franquicias-postgres"
  }
}

resource "aws_elasticache_cluster" "redis" {
  cluster_id           = "franquicias-redis"
  engine               = "redis"
  engine_version       = "7.x"
  node_type            = "cache.t3.micro"
  num_cache_nodes      = 1
  parameter_group_name = "default.redis7"

  tags = {
    Name = "franquicias-redis"
  }
}

resource "aws_ecs_cluster" "main" {
  name = "franquicias-cluster"
}

resource "aws_ecs_task_definition" "app" {
  family                   = "franquicias-app"
  network_mode             = "awsvpc"
  requires_compatibilities = ["FARGATE"]
  cpu                      = "256"
  memory                   = "512"
  execution_role_arn       = aws_iam_role.ecs_execution.arn

  container_definitions = jsonencode([
    {
      name  = "franquicias-app"
      image = var.app_image
      portMappings = [
        {
          containerPort = 8080
          protocol      = "tcp"
        }
      ]
      environment = [
        { name = "DB_HOST",     value = aws_db_instance.postgres.address },
        { name = "DB_PORT",     value = "5432" },
        { name = "DB_NAME",     value = "franquicias" },
        { name = "DB_USER",     value = var.db_user },
        { name = "DB_PASSWORD", value = var.db_password },
        { name = "REDIS_HOST",  value = aws_elasticache_cluster.redis.cache_nodes[0].address },
        { name = "REDIS_PORT",  value = "6379" }
      ]
      logConfiguration = {
        logDriver = "awslogs"
        options = {
          "awslogs-group"         = "/ecs/franquicias"
          "awslogs-region"        = var.aws_region
          "awslogs-stream-prefix" = "ecs"
        }
      }
    }
  ])
}

resource "aws_ecs_service" "app" {
  name            = "franquicias-service"
  cluster         = aws_ecs_cluster.main.id
  task_definition = aws_ecs_task_definition.app.arn
  desired_count   = 1
  launch_type     = "FARGATE"

  network_configuration {
    subnets         = var.subnet_ids
    assign_public_ip = true
    security_groups = [aws_security_group.app.id]
  }
}

resource "aws_security_group" "app" {
  name        = "franquicias-sg"
  description = "Security group for franquicias app"

  ingress {
    from_port   = 8080
    to_port     = 8080
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }
}

resource "aws_iam_role" "ecs_execution" {
  name = "franquicias-ecs-execution"

  assume_role_policy = jsonencode({
    Version = "2012-10-17"
    Statement = [
      {
        Action = "sts:AssumeRole"
        Effect = "Allow"
        Principal = {
          Service = "ecs-tasks.amazonaws.com"
        }
      }
    ]
  })

  managed_policy_arns = [
    "arn:aws:iam::aws:policy/service-role/AmazonECSTaskExecutionRolePolicy"
  ]
}

variable "aws_region" {
  description = "AWS region"
  type        = string
  default     = "us-east-1"
}

variable "db_user" {
  description = "Database user"
  type        = string
  sensitive   = true
}

variable "db_password" {
  description = "Database password"
  type        = string
  sensitive   = true
}

variable "app_image" {
  description = "Docker image for the app"
  type        = string
}

variable "subnet_ids" {
  description = "Subnet IDs for ECS"
  type        = list(string)
}

output "app_url" {
  value = aws_ecs_service.app.id
}
