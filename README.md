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
./gradlew dockerComposeUp
```

### Run without Docker
```
./gradlew build
SERVER_CERTIFICATE=src/test/resources/server.crt SERVER_KEY=src/test/resources/server.key build/image/bin/undertow-vuejs-example
```

## License

This project is licensed under the MIT License - see the [LICENSE.md](LICENSE.md) file for details

[vue-create]: https://cli.vuejs.org/guide/creating-a-project.html#vue-create
[gradle-webpack]: https://guides.gradle.org/running-webpack-with-gradle/
[vue-cli-webpack]: https://cli.vuejs.org/guide/webpack.html
