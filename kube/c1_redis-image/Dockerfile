# create a redis image grom scratch

# base image
FROM alpine

# take image and create container 1
# download and install deps
RUN apk add --update redis
# command added
# new container <1> created

RUN apk add --update gcc
# new container <2> created
# sequence matters, it will be cahched only if the order is non-changed

# take a container <3> created
# run
CMD ["redis-server"]
# startup command added
# new container <3> created
