alert("kuch to kar bhai");

$(document).ready(function(){
  console.log("qwerty")
  alert("I am in ");
  $.ajax({
    url: "http://localhost:8080/api/list/",
    type: "GET"}).done(function(data){
    alert(data);
  });
});

$('#rectButton').click(function(){
  alert('json');
  $.getJSON("http://localhost:8080/api/list/",
      function(data) {
        alert(data);
      });
});
