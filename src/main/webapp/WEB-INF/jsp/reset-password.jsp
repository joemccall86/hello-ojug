<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <meta name="description" content="">
    <meta name="author" content="">
    <title>Forgot Password</title>
    <link href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-beta/css/bootstrap.min.css" rel="stylesheet"
          integrity="sha384-/Y6pD6FV/Vv2HJnA6t+vslU6fwYXjCFtcEpHbNJ0lyAFsXTsjBbfaDjzALeQsN6M" crossorigin="anonymous">
    <link href="https://getbootstrap.com/docs/4.0/examples/signin/signin.css" rel="stylesheet" crossorigin="anonymous"/>
</head>
<body>
<div class="container">
    <form:form cssClass="form-signin" method="post" action="${pageContext.request.contextPath}/reset-password">
    <h2 class="form-signin-heading">Please set a new password</h2>
    <p>
        <label for="password" class="sr-only">Password</label>
        <input type="password" id="password" name="password" class="form-control" placeholder="Password" required>
    </p>
    <p>
        <label for="passwordConfirmation" class="sr-only">Password Confirmation</label>
        <input type="password" id="passwordConfirmation" name="passwordConfirmation" class="form-control" placeholder="Password" required>
    </p>

    <input type="hidden" id="username" name="username" value="${username}" />
    <button class="btn btn-lg btn-primary btn-block" type="submit">Change Password</button>
    <button class="btn btn-lg btn-cancel btn-block" type="reset">Clear</button>
    </form:form>
</body>
</html>