FROM node:15.0-alpine
RUN mkdir -p /usr/src/app
WORKDIR /usr/src/app
COPY . /usr/src/app
EXPOSE 3000
RUN yarn install
ENTRYPOINT yarn dev
