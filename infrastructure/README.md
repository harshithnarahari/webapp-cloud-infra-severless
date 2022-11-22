
# Assignment-8

Github workflow sequence has been added to completely automate bringing API online.
Now, all we have to do is just Push code to main and we have a API ready through the AMI which is created

1. AMI building has been automated completely through Github actions, now all we need to do is raise a Pull request and when it merges with main branch we have AMI ready!
2. Once it's ready go to AWS EC2 instance and check for created AMI
3. Now we can run the Infra-as-code Cloudformation yaml file by passing parameters and Ta-da! we have a instance with API running already thanks to webapp-service.service implemented
4. Once the API is up and running we can test out the end-points on Postman





## Commands used

* packer fmt <filename>
* packer validate <filename>
* packer build <filename>
    
    
    
    
    aws cloudformation create-stack \
  --stack-name <stackname> \
  --template-body <filename> \
  --parameters ParameterKey=EnvironmentName,ParameterValue="ENV_NAME" ParameterKey=VpcCIDR,ParameterValue="" ParameterKey=PublicSubnet1CIDR,ParameterValue="" ParameterKey=PublicSubnet2CIDR,ParameterValue="" ParameterKey=PublicSubnet3CIDR,ParameterValue="" \
  --profile <profilename \
  --region <region-name>


Through Postman:

* public_ip_address:8080/healthz
* public_ip_address:8080/v1/account/ 

## Team Information

| Name | NEU ID | Email Address |
| --- | --- | --- |
| Harshith Narahari| 002963745| narahari.h@northeastern.edu |
