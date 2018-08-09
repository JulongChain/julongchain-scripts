#!/bin/bash
#
# Copyright Dingxuan. All Rights Reserved.
# 
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
# 
#     http://www.apache.org/licenses/LICENSE-2.0
# 
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.

echo
echo " ___               _    _    _    ___    __           __    _   _    _ "
echo "|__|| || || ||    / \  | |\ | |  / __|  /__| ||  ||  /  \  | | | |\ | |"
echo "   || || || ||   || || | | \| | / |__  ||    ||__|| //__|\ | | | | \| |"
echo "  _|| ||_|| ||__ || || | |\ | | | |_|| ||__  ||__|| ||__|| | | | |\ | |"
echo " |_/   \_/  |___| \_/  |_| \|_| |___||  \__| ||  || ||  || |_| |_| \|_|"
echo

SC_DIR="/home/bcia/julongchain"
CMD_PREFIX="java -Djava.ext.dirs=lib -jar /root/julongchain/julongchain.jar"
SC_PATH=${SC_DIR}"/julongchain-sc-java"

CONSENTER_ADDRESS="172.17.0.1:7050"
GROUP_NAME="myGroup"

echo "CMD_PREFIX: "${CMD_PREFIX}
echo "Group: "${GROUP_NAME}

TARGET_NODES=("172.17.0.1:10051" "172.17.0.1:10151" "172.17.0.1:10251")


createGroup() {
 ${CMD_PREFIX} group create -c ${CONSENTER_ADDRESS} -g ${GROUP_NAME} 
 sleep 5
   
 echo "===================== Group '${GROUP_NAME}' created ====================="
 echo
}

joinGroup () {
for node in ${TARGET_NODES[@]};
do
   ${CMD_PREFIX} group join -t ${node} -b ${GROUP_NAME}.block
   echo "===================== ${node} joined Group '${GROUP_NAME}'  ====================="
   echo
   sleep 2
done
}

installSmartContract () {
for node in ${TARGET_NODES[@]};
do
   ${CMD_PREFIX} contract install -t ${node} -n mycc -v 1.0 -p ${SC_PATH}

   echo "===================== SmartContract in ${node} installed ====================="
   echo
   sleep 2
done
}

instantiateSmartContract () {
#for node in ${TARGET_NODES[@]};
#do
   ${CMD_PREFIX} contract instantiate -t ${TARGET_NODES[0]} -c ${CONSENTER_ADDRESS} -g ${GROUP_NAME} -n mycc -v 1.0  -i "{'args':['init','a','100','b','200']}" -P "OR	('Org1MSP.member','Org2MSP.member')"

   echo "===================== SmartContract instantiated ====================="
   echo
   sleep 2

   #${CMD_PREFIX} contract instantiate -t ${TARGET_NODES[1]} -c ${CONSENTER_ADDRESS} -g ${GROUP_NAME} -n mycc -v 1.0  -i "{'args':['init','a','100','b','200']}" -P "OR	('Org1MSP.member','Org2MSP.member')"

   #echo "===================== SmartContract instantiated ====================="
   #echo
   #sleep 2
#done
}

#querySmartContract () {
#${CMD_PREFIX} contract query -g ${GROUP_NAME} -n mycc -i '{"Args":["query","a"]}'
#
#	echo "===================== SmartContract query completed ====================="
#	echo
#}

invokeSmartContract () {
   ${CMD_PREFIX} contract invoke -t ${TARGET_NODES[0]} -c ${CONSENTER_ADDRESS} -g ${GROUP_NAME} -n mycc -i "{'args':['move','a','b','3']}"

   echo "===================== SmartContract invoked ===================== "
   echo
}


# Create group
echo "Creating group-----"
createGroup

# Join group
echo "Joining group-----"
joinGroup

# install SmartContract
echo "Install smart contract-----"
installSmartContract

# Instantiating smart contract
echo "Instantiating smart contract-----"
instantiateSmartContract

# Querying smart contract
# echo "Querying smart contract-----"
# querySmartContract

# Invoking smart contract
# echo "Invoking smart contract-----"
# invokeSmartContract

# Querying smart contract
# echo "Querying smart contract-----"
# querySmartContract

echo
echo "===================== All Completed ====================="
echo

echo
echo " _____   _    _   ____  "
echo "| ____| | |\ | | |  _ \ "
echo "|  _|   | | \| | | | | |"
echo "| |___  | |\ | | | |_| |"
echo "|_____| |_| \|_| |____/ "
echo

exit 0
