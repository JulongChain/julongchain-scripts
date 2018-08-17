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

VERSION="0.8-alpha"
NODE_IMAGE_NAME="julongchain/julongchain-node"
CONSENTER_IMAGE_NAME="julongchain/julongchain-consenter"
CLI_IMAGE_NAME="julongchain/julongchain-cli"
DEMO_IMAGE_NAME="julongchain/julongchain-demo"
BASE_IMAGE_NAME="julongchain/julongchain-baseimage"

sudo docker pull ${NODE_IMAGE_NAME}:${VERSION}
sudo docker pull ${CONSENTER_IMAGE_NAME}:${VERSION}
sudo docker pull ${CLI_IMAGE_NAME}:${VERSION}
sudo docker pull ${DEMO_IMAGE_NAME}:${VERSION}
sudo docker pull ${BASE_IMAGE_NAME}:${VERSION}

echo
echo "===================== All Downloaded ====================="
echo

echo
echo " _____   _    _   ____  "
echo "| ____| | |\ | | |  _ \ "
echo "|  _|   | | \| | | | | |"
echo "| |___  | |\ | | | |_| |"
echo "|_____| |_| \|_| |____/ "
echo

exit 0
