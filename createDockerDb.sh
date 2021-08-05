docker run \
    -p 3306:3306 \
    --name accsaber-db \
    -e MYSQL_ROOT_PASSWORD=root \
    -e MYSQL_DATABASE=accsaber_db \
    -e MYSQL_USER=acc \
    -e MYSQL_PASSWORD=acc \
    -d mariadb:10.5 \
    --character-set-server=utf8mb4 \
    --collation-server=utf8mb4_unicode_ci