#!/bin/bash
#
# Copyright IBM Corp. All Rights Reserved.
#
# SPDX-License-Identifier: Apache-2.0
#


jq --version > /dev/null 2>&1
if [ $? -ne 0 ]; then
	echo "Please Install 'jq' https://stedolan.github.io/jq/ to execute this script"
	echo
	exit 1
fi

starttime=$(date +%s)

# Print the usage message
function printHelp () {
  echo "Usage: "
  echo "  ./testAPIs.sh -l golang|node"
  echo "    -l <language> - chaincode language (defaults to \"golang\")"
}
# Language defaults to "golang"
LANGUAGE="golang"

# Parse commandline args
while getopts "h?l:" opt; do
  case "$opt" in
    h|\?)
      printHelp
      exit 0
    ;;
    l)  LANGUAGE=$OPTARG
    ;;
  esac
done

echo "POST request Enroll on Org1  ..."
echo
ORG1_TOKEN=$(curl -s -X POST \
  http://localhost:4399/users \
  -H "content-type: application/x-www-form-urlencoded" \
  -d 'username=qx1&orgName=Org1')
echo $ORG1_TOKEN
ORG1_TOKEN=$(echo $ORG1_TOKEN | jq ".token" | sed "s/\"//g")
echo
echo "ORG1 token is $ORG1_TOKEN"
echo
echo "POST request Enroll on Org2 ..."
echo
ORG2_TOKEN=$(curl -s -X POST \
  http://localhost:4399/users \
  -H "content-type: application/x-www-form-urlencoded" \
  -d 'username=qr1&orgName=Org2')
echo $ORG2_TOKEN
ORG2_TOKEN=$(echo $ORG2_TOKEN | jq ".token" | sed "s/\"//g")
echo
echo "ORG2 token is $ORG2_TOKEN"
echo
echo

echo "write data"
# input 4 parameters
# property1 to property3 is the property of your data
# data is the text you want to write to blockchain
# you should use the token in previous step token to take in
# return the transaction ID
echo
TRX_ID=$(curl -s -X POST \
  http://localhost:4399/channels/mychannel/chaincodes/mycc \
  -H "authorization: Bearer $ORG1_TOKEN" \
  -H "content-type: application/json" \
  -d '{
	"peers": ["peer0.org1.example.com","peer1.org1.example.com"],
	"fcn":"invoke",
	"args":["property1","property2","property3","Try second script to test."]
}')
echo "Transaction ID is $TRX_ID"
echo
echo

echo "query the data by properties"
# input the 3 properties of the data
# output the data
# the properties is split by %
# you should use the token in previous step token to take in
echo
curl -s -X GET \
  "http://localhost:4399/channels/mychannel/chaincodes/mycc?peer=peer0.org1.example.com&fcn=query&args=property1%property2%property3" \
  -H "authorization: Bearer $ORG1_TOKEN" \
  -H "content-type: application/json"
echo
echo


echo "Total execution time : $(($(date +%s)-starttime)) secs ..."
echo "Script soul_2 ends ..."