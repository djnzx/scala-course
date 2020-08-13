# https://hub.docker.com => Explore
#FROM alpine
#FROM node:10.22
FROM node:alpine

# destination folder in the container by default
WORKDIR /usr/simplewebapp

# local path, relative to build context, container path
COPY ./package.json ./
RUN npm install

COPY ./index.js ./


CMD ["npm", "start"]
