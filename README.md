# WebExecutionSITAC

This repository contains a web application that supports the execution of service compositions built by end-users. The
core component of the web application is the [Activiti](http://activiti.org/) Engine, which supports the execution of
service compositions that are defined as process models compliant with the BPMN 2.0 standard. During the execution of
these process models, the information exchanged (in JSON) between the Activiti Engine and the server allows the web
application to dynamically generate graphical interfaces for the user. These interfaces enable the invocation of the
web services that are defined in the process model.

The web application that is contained in this repository was developed in the context of an ITEA 3 project: [SITAC] (https://itea3.org/project/sitac.html). For the definition of service compositions, SITAC provides a mobile end-user
tool, which allows users to define BPMN 2.0 process models without the need to have any technical knowledge about
this standard or Activiti.
