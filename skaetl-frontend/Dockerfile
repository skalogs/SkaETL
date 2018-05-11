FROM node:8.8.1

ENV NODE_ENV "development"
ENV API_BASE_URL "http://localhost:8090"

RUN mkdir -p /usr/src/app
WORKDIR /usr/src/app

COPY package.json /usr/src/app/

COPY . /usr/src/app

EXPOSE 5555

CMD ["npm", "run", "prod"]
