FROM node:alpine

WORKDIR /usr/simplecounter

COPY ./package.json .
RUN npm install
COPY ./index.js .

CMD ["npm", "start"]
