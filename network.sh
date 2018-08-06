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

CONSENTER_NAME="Julongchain-Consenter"
NODE_NAME1="Julongchain-Node1"
NODE_NAME2="Julongchain-Node2"
NODE_NAME3="Julongchain-Node3"
CLI_NAME="Julongchain-CLI"

CONFIG_DIR="/home/bcia/julongchain"
CONSENTER_CONFIG_NAME="consenter-config"
NODE1_CONFIG_NAME="node1-config"
NODE2_CONFIG_NAME="node2-config"
NODE3_CONFIG_NAME="node3-config"

SC_DIR="/home/bcia/julongchain"

consenter_id=`sudo docker run --name=${CONSENTER_NAME} -d -p 7050:7050 -p 7060:7060 -p 7070:7070 -v ${CONFIG_DIR}/${CONSENTER_CONFIG_NAME}:/root/julongchain/config -t julongchain/julongchain-consenter`

echo "Consenter containerId: "${consenter_id}

sleep 5

node1_id=`sudo docker run --name=${NODE_NAME1} -d -p 10051:7051 -p 10052:7052 -p 10060:7060 -p 10070:7070 -v ${CONFIG_DIR}/${NODE1_CONFIG_NAME}:/root/julongchain/config -v /var/run:/var/run -t julongchain/julongchain-node`
node2_id=`sudo docker run --name=${NODE_NAME2} -d -p 10151:7051 -p 10152:7052 -p 10160:7060 -p 10170:7070 -v ${CONFIG_DIR}/${NODE2_CONFIG_NAME}:/root/julongchain/config -t julongchain/julongchain-node`
node3_id=`sudo docker run --name=${NODE_NAME3} -d -p 10251:7051 -p 10252:7052 -p 10260:7060 -p 10270:7070 -v ${CONFIG_DIR}/${NODE3_CONFIG_NAME}:/root/julongchain/config -t julongchain/julongchain-node`

echo "Node1 containerId: "${node1_id}
echo "Node2 containerId: "${node2_id}
echo "Node3 containerId: "${node3_id}

cli_id=`sudo docker run --name=${CLI_NAME} -d -v ${SC_DIR}:${SC_DIR} -t julongchain/julongchain-cli`
echo "CLI containerId: "${cli_id}

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
