<html>
<head>
<title>Welcome</title>
<link href="webjars/bootstrap/3.3.6/css/bootstrap.min.css"
rel="stylesheet">
</head>
<body>
   <div class="container">
    <table id="scanDetailTable" class="table table-striped" style="width:100%">
    <caption>Scan Details </caption>
    <thead>
        <th>URL</th>
        <th>Redirection URL</th>
        <th>Submitted On</th>
        <th>Website Title</th>
        <th>Website Body Content</th>
        <th>Image Count</th>
        <th>Links Count</th>
        </tr>
    </thead>
</table>
<div>
<a id="backButton" class="btn btn-default" href="/">Back</a>
</div>
<script src="webjars/jquery/1.9.1/jquery.min.js"></script>
    <script src="webjars/bootstrap/3.3.6/js/bootstrap.min.js"></script>
    <link type="text/css" rel="stylesheet" th:href="@{https://cdn.datatables.net/1.10.16/css/dataTables.bootstrap.min.css}" />
    <script type="text/javascript" src="https://cdn.datatables.net/1.10.16/js/jquery.dataTables.min.js"></script>
    <script type="text/javascript" src="https://cdn.datatables.net/1.10.16/js/dataTables.bootstrap.min.js"></script>
    <script type="text/javascript">

      $(document).ready(function(){
        $.ajax({
          url: "http://localhost:8080/api/scan/"+${id},
          type: "GET",
          dataType: "json"
        }).done(function(data){
          console.log(data);
          $(document).ready(function() {
            $('#scanDetailTable').dataTable( {
              searching: false,
              paging: false,
              data: data,
              "columns": [
                {"data": "url"},
                { "data": "redirectionUrl"},
                { "data": "submittedOn" },
                { "data": "title" },
                { "data": "body"},
                { "data": "imgCount"},
                { "data":"linksCount"}
              ]
            });
          });
        });
      });
</script>
</div>
</body>
</html>
