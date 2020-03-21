# Undertow+Vue.js Example

## Build
```
./gradlew build
```

### Build only web assets
```
./gradlew webpack
```

## Run locally
```
SERVER_CERTIFICATE=src/test/resources/server.crt SERVER_KEY=src/test/resources/server.key build/image/bin/undertow-vuejs-example
```

## Project setup
```
npm install
```

### Compiles and hot-reloads for development
```
npm run serve
```

### Compiles and minifies for production
```
npm run build
```

### Lints and fixes files
```
npm run lint
```

## License

This project is licensed under the MIT License - see the [LICENSE.md](LICENSE.md) file for details

[vue-create]: https://cli.vuejs.org/guide/creating-a-project.html#vue-create
[gradle-webpack]: https://guides.gradle.org/running-webpack-with-gradle/
[vue-cli-webpack]: https://cli.vuejs.org/guide/webpack.html
