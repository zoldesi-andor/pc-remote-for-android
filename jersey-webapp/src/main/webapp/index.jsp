<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title></title>
    <script type="text/javascript" language="javascript" src="http://ajax.aspnetcdn.com/ajax/jQuery/jquery-1.6.4.min.js" ></script>
</head>
<body>

<h2>Jersey RESTful Web Application!</h2>
<p><a href="webresources/layouts/list">Jersey resource</a>
<p>Visit the <a href="http://jersey.java.net">Project Jersey website</a>
for more information on Jersey!

    <p id="ptext">
        <label for="text">Text:</label>
        <input id="text" name="text" type="text" />
    </p>
    <p>
        <label for="type">Type:</label>
        <select id="type">
            <option>Hello</option>
            <option>Bello</option>
        </select>
    </p>
    <a id="add" href="">Add new line</a>
    <div id="main">
    </div>

    <input id="btnSend" type="button" value="Send" />

    <div id="result">

    </div>

    <div id="puffer">
        <table>

        </table>
    </div>

    <script type="text/javascript">
        var j = new Array();
        i = 0;
        $(document).ready(function () {

            $.get('http://192.168.1.100:8080/jersey-webapp/webresources/layouts/list', function (data) {
                debugger;
                $("#result").html(data);

            });

            /////////////////////////////////////
            // Disable not used elements by type
            //
            $("#type").change(function (event) {
                if (this.value == "Bello") {
                    $("#ptext").hide();
                } else {
                    $("#ptext").show();
                }
            });

            //////////////
            // Send button
            //
            $("#btnSend").click(function (event) {
                debugger;
                var data = $("#puffer").html();
            });

            /////////////////
            // New Row Button
            //
            $("#add").click(function (event) {
                i++;
                $("#main").append(
                    "<p id=\"p" + i + "\">" +
                        "<input id=\"d" + i + "\" name=" + i + " type=\"button\" value=\"Delete\"/>" +
                        "<input id=\"b" + i + "\" name=" + i + " type=\"button\" value=\"Add item\"/>:" +
                    "</p>");

                $("table").append("<row id=\"r" + i + "\"></row>");

                ///////////////
                // Add new item
                //
                $("#b" + i).click(function (event) {
                    index = event.target.name;
                    if (j[index] == null)
                        j[index] = 1;
                    else
                        j[index]++;

                    var text = $("#text").val();
                    $("#text").val("");
                    var type = $("#type").val();

                    $("#p" + index).append(
                        "<input " +
                            "id=\"btn" + index + j[index] + "\" " +
                            "name=" + index + "|" + j[index] + " " +
                            "type=\"button\" " +
                            "value=\"" + text + "\"/>");

                    $("#r" + index).append(
                        "<button " +
                            "id=\"rcbtn" + index + j[index] + "\" " +
                            "name=" + index + "|" + j[index] + " " +
                            "type=\"" + type + "\" " +
                            "value=\"" + text + "\"/>");

                    ///////////////
                    // Delete Item
                    //
                    $("#btn" + index + j[index]).click(function (event) {
                        var t = this.name.split("|");
                        $("#btn" + t[0] + t[1]).remove();
                        $("#rcbtn" + t[0] + t[1]).remove();
                    });
                });

                //////////////
                // Delete Row
                //
                $("#d" + i).click(function (event) {
                    $("#p" + this.name).remove();
                    $("#r" + this.name).remove();
                });
                event.preventDefault();
            });
        })
    </script>
</body>
</html>