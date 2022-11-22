#!/bin/sh
echo "Installing Java"
wget https://download.oracle.com/java/19/latest/jdk-19_linux-x64_bin.deb
sudo apt-get -qqy install ./jdk-19_linux-x64_bin.deb
sudo update-alternatives --install /usr/bin/java java /usr/lib/jvm/jdk-19/bin/java 1919

# echo "Installing and bringing up PostgreSQL"
# sudo apt install -y postgresql postgresql-contrib
# sudo systemctl start postgresql.service
# sudo -u postgres psql --command="ALTER ROLE postgres WITH PASSWORD 'Qwerty123##';" --command="\du"
# sudo systemctl restart postgresql
# sudo -u postgres createdb --owner=postgres postgres


sudo mkdir test/
sudo chmod 777 test/

sudo cp /tmp/webapp-0.0.1-SNAPSHOT.jar /home/ubuntu/test/webapp-0.0.1-SNAPSHOT.jar

sudo cp /tmp/cloudwatch-agent-config.json 
# echo "Starting the Spring Boot application as service"
sudo cp /tmp/webapp-service.service /etc/systemd/system/webapp-service.service
# sudo systemctl daemon-reload
# sudo systemctl enable webapp-service.service
# sudo systemctl start webapp-service.service
sudo curl -o /root/amazon-cloudwatch-agent.deb https://s3.amazonaws.com/amazoncloudwatch-agent/debian/amd64/latest/amazon-cloudwatch-agent.deb
sudo dpkg -i -E /root/amazon-cloudwatch-agent.deb
sudo cp /tmp/cloudwatch-agent-config.json /opt/aws/amazon-cloudwatch-agent/etc/amazon-cloudwatch-agent.json
sudo /opt/aws/amazon-cloudwatch-agent/bin/amazon-cloudwatch-agent-ctl \
    -a fetch-config \
    -m ec2 \
    -c file:/opt/aws/amazon-cloudwatch-agent/etc/amazon-cloudwatch-agent.json \
    -s
sudo service amazon-cloudwatch-agent start