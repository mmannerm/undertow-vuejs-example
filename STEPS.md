# Steps to re-produce the project

1. Initialize Gradle project
   ```
     gradle init
   ```
2. Scaffold vue.js project
   ```
     vue create -d undertow-vuejs-example
     mkdir -p src/main/webapp
     mv undertow-vuejs-example/*.js* .
     mv undertow-vuejs-example/src/* src/main/webapp
     mv undertow-vuejs-example/public .
     cat <<EOF >vue.config.js
     module.exports = {
       outputDir: 'build/resources/main/static',
       pages: {
          index: 'src/main/webapp/main.js'
       }
     }
     EOF
     echo "node_modules" >> .gitignore
     rm -rf undertow-vuejs-example
   ```
3. Test everything works
   ```
     npm install
     npm run build
   ```
