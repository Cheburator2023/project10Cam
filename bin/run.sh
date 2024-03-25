docker stop camunda
docker rm camunda

docker run -d \
        --name camunda \
        --restart always \
        --net vtb \
        --env-file .env \
        -v /home/rostislav/Projects/VTB/camunda/configuration/:/configuration/ \
        -p 8085:8080 \
        -p 9995:9990 \
        camunda


docker logs -f camunda
