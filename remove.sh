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

echo "Stop:"
sudo docker stop $(docker ps -q)

echo "Remove:"
sudo docker rm $(docker ps -aq)

echo "Remove image:"
sudo docker rmi node1-mycc-1.0
sudo docker rmi node2-mycc-1.0
sudo docker rmi node3-mycc-1.0

sudo rm -rf /home/bcia/julongchain/node1/data
sudo rm -rf /home/bcia/julongchain/node2/data
sudo rm -rf /home/bcia/julongchain/node3/data

sudo rm -rf /home/bcia/julongchain/node1/logs
sudo rm -rf /home/bcia/julongchain/node2/logs
sudo rm -rf /home/bcia/julongchain/node3/logs

echo
echo "===================== All Removed ====================="
echo

echo
echo " _____   _    _   ____  "
echo "| ____| | |\ | | |  _ \ "
echo "|  _|   | | \| | | | | |"
echo "| |___  | |\ | | | |_| |"
echo "|_____| |_| \|_| |____/ "
echo

exit 0
