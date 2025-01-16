FROM node:22.1.0 AS Production
LABEL authors="Antonio Ceruso"
LABEL authors="Geraldine Montella"
WORKDIR /Brdige/bridge-frontend

COPY package.json .
COPY package-lock.json .

RUN npm install
RUN npm install date-fns
COPY . .

EXPOSE 5174

CMD ["npm", "run", "dev"]

