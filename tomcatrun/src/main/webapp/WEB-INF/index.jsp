<!DOCTYPE html>
<html>
   <head>
      <title>Swagger UI</title>
      <link rel="stylesheet" type="text/css" href="/api/swagger-ui.css">
      <script src="/api/swagger-ui-bundle.js"></script>
      <script src="/api/swagger-ui-standalone-preset.js"></script>
   </head>
   <body>
      <div id="ht"></div>
      <script>
         window.onload = function() {
            // Build a system
            const ui = SwaggerUIBundle({
               url: "/api/swagger.json",
               dom_id: "#swagger-ui",
               presets: [
                  SwaggerUIBundle.presets.apis,
                  SwaggerUIStandalonePreset
               ],
               layout: "StandaloneLayout"
            })
         }
      </script>
   </body>
</html>