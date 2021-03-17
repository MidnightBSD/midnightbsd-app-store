# midnightbsd-app-store
[![FOSSA Status](https://app.fossa.io/api/projects/git%2Bgithub.com%2FMidnightBSD%2Fmidnightbsd-app-store.svg?type=shield)](https://app.fossa.io/projects/git%2Bgithub.com%2FMidnightBSD%2Fmidnightbsd-app-store?ref=badge_shield)

![CodeQL](https://github.com/MidnightBSD/midnightbsd-app-store/workflows/CodeQL/badge.svg)

MidnightBSD APP Store API

Provides the app store front end and backend rest API.  

The appstore is avaialble online at https://app.midnightbsd.org/

## Dependencies

This project currently requires elasticsearch, postgresql and redis. It's written in Java with spring boot. The front end is a mix of thymeleaf templates and angular.js.

This application also makes external calls to Magus, the MidnightBSD package cluster software to determine what apps are avaialble for a given OS version and architecture.  This app pulls the latest package build that was marked as "blessed" and uses that to populate all of the apps avaialble currently for a given release. 

The web frontend is written in Angular.JS and the backend is
Spring Boot 2.

Requires:
* Redis 3.x or later
* Java 11 or later
* PostgreSQL 9.x or newer
* ElasticSearch 6.7.x or newer

## clients
In addition to the website, the graphical package manager (mport-manager), will call the app store to get user ratings for apps via the rest API. 


## License
[![FOSSA Status](https://app.fossa.io/api/projects/git%2Bgithub.com%2FMidnightBSD%2Fmidnightbsd-app-store.svg?type=large)](https://app.fossa.io/projects/git%2Bgithub.com%2FMidnightBSD%2Fmidnightbsd-app-store?ref=badge_large)
