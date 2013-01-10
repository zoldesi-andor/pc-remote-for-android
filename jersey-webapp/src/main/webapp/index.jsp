
<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="utf-8">
    <title>Bootstrap, from Twitter</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="">
    <meta name="author" content="">

    <!-- Le styles -->
    <link href="./css/bootstrap.css" rel="stylesheet">
    <style>
      body {
        padding-top: 60px; /* 60px to make the container go all the way to the bottom of the topbar */
      }
    </style>
    <link href="./css/bootstrap-responsive.css" rel="stylesheet">

    <!-- HTML5 shim, for IE6-8 support of HTML5 elements -->
    <!--[if lt IE 9]>
      <script src="http://html5shim.googlecode.com/svn/trunk/html5.js"></script>
    <![endif]-->
  </head>

  <body>

    <div class="navbar navbar-inverse navbar-fixed-top">
      <div class="navbar-inner">
        <div class="container">
          <a class="btn btn-navbar" data-toggle="collapse" data-target=".nav-collapse">
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
          </a>
          <a class="brand" href="#">Template Repository</a>
          <div class="nav-collapse collapse">
            <ul class="nav">
              <li class="active"><a href="#">Template List</a></li>
              <li><a href="#about">New Template</a></li>
            </ul>
          </div><!--/.nav-collapse -->
        </div>
      </div>
    </div>
    <div class="container">

        <h1>Bootstrap starter template</h1>
        <p>Use this document as a way to quick start any new project.<br> All you get is this message and a barebones HTML document.</p>

<div class="row">
        <table class="table table-striped span4" data-bind='visible: listViewModel.selectedItem() == null'>
            <thead>
                <tr>
                    <th>#</th>
                    <th>Name</th>
                </tr>
            </thead>
            <tbody data-bind="foreach: listViewModel.layouts">
                <tr data-bind="click: $root.listViewModel.selectItem">
                    <td data-bind="text: id"></td>
                    <td data-bind="text: name"></td>
                </tr>
            </tbody>
        </table>

        <div class="span8" data-bind='visible: listViewModel.selectedItem() != null'>
            <form>
                <p>
                    <input id="name" class="input-block-level" data-bind="value: listViewModel.name" type="text" />
                </p>
                <p>
                    <textarea id="xml" class="input-block-level" data-bind="value: listViewModel.text" cols="60"></textarea>
                </p>
                <p>
                    <a data-bind="click: listViewModel.saveItem" href="#" class="btn btn-success">Save</a>
                    <a data-bind="click: listViewModel.deleteItem" href="#" class="btn btn-danger">Delete</a>
                    <a data-bind="click: listViewModel.cancelEdit" href="#" class="btn">Cancel</a>
                </p>
            </form>

        </div>

</div>

    </div> <!-- /container -->

    <!-- Le javascript
    ================================================== -->
    <!-- Placed at the end of the document so the pages load faster -->
    <script src="./js/jquery-1.8.3.min.js"></script>
    <script src="./js/bootstrap.min.js"></script>
    <script src="./js/knockout-2.2.0.js"></script>
    <script src="./js/sammy.min.js"></script>
    <script src="./js/app.js"></script>

  </body>
</html>
