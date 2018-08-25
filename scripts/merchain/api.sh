#!/bin/sh
# ssh ubuntu@host -p port
localhost=
port=
#=============== regist user =====================
#replace USRNAME to the username you want.
#ruturn a token
#=======================================================
echo
ORG1_TOKEN=$(curl -s -X POST \
  http://${localhost}:4000/users \
  -H "content-type: application/x-www-form-urlencoded" \
  -d 'username=souler&orgName=Org1')
echo $ORG1_TOKEN
ORG1_TOKEN=$(echo $ORG1_TOKEN | jq ".token" | sed "s/\"//g")
echo

#================== write data =========================
#input 4 parameters
#property1 to property3 is the property of your data
#data is the text you want to write to blockchain
#you should use the token in previous step token to take in
#return the transaction ID
#=======================================================
echo
TRX_ID=$(curl -s -X POST \
  http://${localhost}:4000/channels/mychannel/chaincodes/mycc \
  -H "authorization: Bearer $ORG1_TOKEN" \
  -H "content-type: application/json" \
  -d '{
	"peers": ["peer0.org1.example.com","peer1.org1.example.com"],
	"fcn":"invoke",
	"args":["property1","property2","property3","data"]
}')
