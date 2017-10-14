<html>
<head>
  <title>Wikipedia Expansion Tool</title>
  <style type="text/css">
  body {
    /* background-image: url('https://cdn.pixabay.com/photo/2017/09/28/21/50/background-2797157_960_720.jpg'); */
  }
  </style>
  <link href="resources/img/favicon.ico" rel="icon" type="image/x-icon">
</head>
<body>
  <br>
  <div style="text-align:center">
    <h1>
      Welcomes to the WIKIPEDIA EXPANSION TOOL
    </h1>
    <h2>
      Now With Jetty!!!
    </h2>    
    <h3>
      Insert your notions and push "Start Wikipedia Expansion" 
    </h3>
  </div>
  <div>  
    <form name="notions-forms" action="${pageContext.request.contextPath}/expand" method="post" style="text-align:center">
      <input name="notion1" type="text" />
      <input name="notion2" type="text" />
      <input name="notion3" type="text" />
      <input name="notion4" type="text" />
      <input name="notion5" type="text" />
      <br>
      <br>
      <input type="submit" value="Start Wikipedia Expansion">
    </form>
  </div>
</body>
</html>