#!/bin/bash

# Exit on error
set -e

echo "Starting deployment to Minikube..."

# Check if Minikube is running
if ! minikube status | grep -q "Running"; then
  echo "Starting Minikube..."
  minikube start
else
  echo "Minikube is already running."
fi

# Build the application and Docker image using Spring Boot Maven plugin
echo "Building the application and Docker image..."
mvn spring-boot:build-image

# Load the image into Minikube
echo "Loading image into Minikube..."
minikube image load unicorn-spring-ai-agent:0.0.1-SNAPSHOT

# Extract AWS credentials from ~/.aws/credentials or ~/.aws/config
echo "Extracting AWS credentials from AWS config files..."
AWS_PROFILE=${AWS_PROFILE:-default}
echo "Using AWS profile: $AWS_PROFILE"

# Check if AWS CLI is installed
if ! command -v aws &> /dev/null; then
  echo "AWS CLI is not installed. Please install it first."
  exit 1
fi

# Extract credentials using AWS CLI
AWS_ACCESS_KEY_ID=$(aws configure get aws_access_key_id --profile $AWS_PROFILE)
AWS_SECRET_ACCESS_KEY=$(aws configure get aws_secret_access_key --profile $AWS_PROFILE)
AWS_REGION=$(aws configure get region --profile $AWS_PROFILE)

# Use default region if not found
if [ -z "$AWS_REGION" ]; then
  AWS_REGION="us-east-1"
  echo "AWS region not found in profile $AWS_PROFILE, using default: $AWS_REGION"
else
  echo "Using AWS region: $AWS_REGION"
fi

# Check if credentials were found
if [ -z "$AWS_ACCESS_KEY_ID" ] || [ -z "$AWS_SECRET_ACCESS_KEY" ]; then
  echo "AWS credentials not found in ~/.aws/credentials or ~/.aws/config for profile $AWS_PROFILE."
  echo "Please configure your AWS credentials using 'aws configure' or specify a different profile:"
  echo "export AWS_PROFILE=your-profile-name"
  echo "Then run this script again."
  exit 1
fi

# Export the credentials as environment variables for the secret creation
export AWS_ACCESS_KEY_ID
export AWS_SECRET_ACCESS_KEY

# Create AWS credentials secret
echo "Creating AWS credentials secret..."
envsubst < k8s/aws-secret.yaml | kubectl apply -f -

# Apply Kubernetes manifests (excluding postgres.yaml)
echo "Deploying to Kubernetes..."
kubectl apply -f k8s/deployment.yaml -f k8s/service.yaml -f k8s/configmap.yaml -f k8s/ingress.yaml

# Wait for deployment to be ready
echo "Waiting for deployment to be ready..."
kubectl rollout status deployment/unicorn-spring-ai-agent

echo "Deployment completed successfully!"
echo "To access the application, you have three options:"
echo ""
echo "Option 1 (Recommended with Ingress addon): Use the Ingress controller:"
echo "  MINIKUBE_IP=\$(minikube ip)"
echo "  echo \"\$MINIKUBE_IP unicorn.local\" | sudo tee -a /etc/hosts"
echo "  # Then access the application at http://unicorn.local"
echo ""
echo "Option 2: Run minikube tunnel in a separate terminal:"
echo "  minikube tunnel"
echo "  # Then access the application using the external IP:"
echo "  kubectl get services  # Wait for external IP to be assigned"
echo "  SERVICE_URL=\$(kubectl get service unicorn-spring-ai-agent -o jsonpath='{.status.loadBalancer.ingress[0].ip}'):8080"
echo "  curl \$SERVICE_URL  # Or open in browser"
echo ""
echo "Option 3: Simply run:"
echo "  minikube service unicorn-spring-ai-agent"
echo ""
echo "For more detailed instructions, please refer to the README.md file."
