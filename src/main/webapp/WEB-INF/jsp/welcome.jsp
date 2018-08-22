<html>
<head>
	<title>Welcome</title>
	<link href="webjars/bootstrap/3.3.6/css/bootstrap.min.css"
				rel="stylesheet">
</head>
<body>
<div class="container">
	<div class="form-group">
		<h3>HTML SCANNER</h3>
		<br/>
		<p id="errorMsg" style="color: red"></p>
		<label for="url">Submit an URL for Scanning :: </label>
		<input type="text" class="form-control input-sm " id="url" width="30px">
		<br/>
		<a id="submitUrl" class="btn btn-default">Submit URL</a>
	</div>

	<table id="scanTable" class="table table-striped" style="width:100%">
		<caption>Last 10 Scanned URL are as Follows :: </caption>
		<thead>
			<th>Id</th>
			<th>Hostname</th>
			<th>Scan Status</th>
			<th>IP Address</th>
			<th>Title</th>
			<th>Submitted On</th>
		</tr>
		</thead>
	</table>
	<script src="webjars/jquery/1.9.1/jquery.min.js"></script>
	<script src="webjars/bootstrap/3.3.6/js/bootstrap.min.js"></script>
	<link type="text/css" rel="stylesheet" th:href="@{https://cdn.datatables.net/1.10.16/css/dataTables.bootstrap.min.css}" />
	<script type="text/javascript" src="https://cdn.datatables.net/1.10.16/js/jquery.dataTables.min.js"></script>
	<script type="text/javascript" src="https://cdn.datatables.net/1.10.16/js/dataTables.bootstrap.min.js"></script>
	<script type="text/javascript">

    $(document).ready(function(){
      $('errorMsg').hide()
      $.ajax({
        url: "http://localhost:8080/api/list/",
        type: "GET",
        dataType: "json"
      }).done(function(data){
        $(document).ready(function() {
          $('#scanTable').dataTable( {
            searching: false,
						paging: false,
            data: data,
            "order": [[ 5, "desc" ]],
            "columns": [
							{"data": "id",
                "orderSequence": "asc",
                "visible": false},
              { "data": "hostname",
                "render": function(data, type, row, meta){
                  if(type === 'display'){
                    data = '<a href="/scan?id=' + row.id + '">' + data + '</a>';
                  }
                  return data;
                }
							},
              { "data": "scanStatus" },
              { "data": "ipAddress" },
							{ "data": "title"},
							{"data": "submittedOn"}
            ]
          });
        });
      });
    });

    $('#submitUrl').click(function() {
      var body = $("input").val();
      $.ajax({
        traditional: true,
        url: "http://localhost:8080/api/submit/",
        dataType: "json",
        type: "POST",
        data: body,
        contentType: "application/json; charset=utf-8",
        success: function (data) {
          var dat = data;
          if (dat.hasOwnProperty("code")) {
            $('p#errorMsg').show();
            $('p#errorMsg').text(dat.message);
					} else {
            setTimeout(function () {
              location.reload();
            }, 500);
          }
        },
        error: function (data) {
          console.log("failure"+ data);
        }
      });
    });
	</script>
</div>
</body>
</html>
