docker run -i --rm -p 8180:8080 -p 5005:5005 -e JAVA_ENABLE_DEBUG="true" -e "SIIB_DB_HOST=172.24.64.1" -e "SIIB_DB_PORT=1521" -e "SIIB_DB_SID=XE" -e "SIIB_DB_USER=SIIBC_Collectif" -e "SIIB_DB_PASSWORD=collectif" mic-collectif-api:latest