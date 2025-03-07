cd SessionScheduler
gradle build
docker build -f SessionScheduler/Dockerfile -t ak47001/session-scheduler .
docker image push ak47001/session-scheduler
