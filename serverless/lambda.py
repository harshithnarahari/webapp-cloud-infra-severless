import boto3
from botocore.exceptions import ClientError

def send_email(email, token):
    SENDER = "webapp@prod.harshithnarahari.me" # must be verified in AWS SES Email
    RECIPIENT = "harshithnarahari@gmail.com" # must be verified in AWS SES Email

    # If necessary, replace us-west-2 with the AWS Region you're using for Amazon SES.
    AWS_REGION = "us-east-1"

    # The subject line for the email.
    SUBJECT = "Verification Email"

    # The email body for recipients with non-HTML email clients.
    authlink = "http://prod.harshithnarahari.me/v1/verifyaccount?email=" + email + "&token=" + token
    BODY_TEXT = ("You the link provided to verify yourself \r\n"
                "This email was sent with Amazon SES using the "
                "AWS SDK for Python (Boto)."
                )
                
    # The HTML body of the email.
    BODY_HTML ="Click here to verify: "+authlink            

    # The character encoding for the email.
    CHARSET = "UTF-8"

    # Create a new SES resource and specify a region.
    client = boto3.client('ses',region_name=AWS_REGION)

    # Try to send the email.
    try:
        #Provide the contents of the email.
        response = client.send_email(
            Destination={
                'ToAddresses': [
                    RECIPIENT,
                ],
            },
            Message={
                'Body': {
                    'Html': {
        
                        'Data': BODY_HTML
                    },
                    'Text': {
        
                        'Data': BODY_TEXT
                    },
                },
                'Subject': {

                    'Data': SUBJECT
                },
            },
            Source=SENDER
        )
 
    # Display an error if something goes wrong.	
    except ClientError as e:
        print(e.response['Error']['Message'])
    else:
        print("Email sent! Message ID:"),
        print(response['MessageId'])

def lambda_handler(event, context):
    # TODO implement
    message = event['Records'][0]['Sns']['Message']
    print("From SNS: " + message)
    valuelist = message.split(",")
    print(valuelist)
    emailadd = valuelist[0]
    token = valuelist[1]
    print (emailadd, token)
    send_email(emailadd, token)