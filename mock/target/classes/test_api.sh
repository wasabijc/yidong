#!/bin/bash

# 基础URL
BASE_URL="http://localhost:8080/api"
REGION_ID="3747835037114957824"

# 1. 测试实时数据接口
echo -e "\n测试实时数据接口:"
curl -s -X GET "$BASE_URL/realtime/$REGION_ID" | jq

# 2. 测试历史数据接口
echo -e "\n测试历史数据接口:"
curl -s -X GET "$BASE_URL/historical/$REGION_ID?start=2023-01-01T00:00:00Z&end=2023-12-31T23:59:59Z" | jq

# 3. 测试OpenAPI接口
echo -e "\n测试OpenAPI接口:"
curl -s -X GET "$BASE_URL/open/last5min?regionId=$REGION_ID" | jq