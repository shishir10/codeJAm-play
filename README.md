# CodeJam

This is a simple application using Play Framework for Jigsaw hackathon

## Features
* Sign Up
* Sign In
* Reset password
* Settings Page
* Change email from Settings
* Display multiple questions with Data-Set links and submissions page links
* Each question has seperate submission pages and data.
* The best score out of all the submissions is stored.
* The upload and evaluation happens asynchronously, integrated with AJAX
* Leaderboard displays all the users of same college, sorted via percentage.

## Application details
* No clear password stored in db
* Secure workflow to reset password
* Secure workflow to reset email
* I18n example (en, fr)
* Using Typesafe Plugin Mailer : https://github.com/typesafehub/play-plugins/tree/master/mailer
* Using Twitter Bootstrap 3 : http://twitter.github.com/bootstrap/
* Using Font-Awesome 4 : http://fortawesome.github.io/Font-Awesome/icons/
* Using Less and CoffeeScript
* Using a password strength checker

## Try
* Download Activator from http://www.playframework.org/
* Open a terminal in PlayStartApp directory and exec `activator run`
* Generate Scala Doc & Javadoc with exec `activator app-doc` (task app-doc add in Build.scala file)

## Activator
* See https://typesafe.com/activator/template/PlayStartApp

## Documentation
* Failing with Passwords (a presentation on issues in user authentication) : http://tersesystems.com/2012/02/17/failing-with-passwords
* Everything you ever wanted to know about secure password reset : http://www.troyhunt.com/2012/05/everything-you-ever-wanted-to-know.html

## Licence
* BSD. See LICENSE file

## Contact
instagram: @shishir10
