# WebExecutionSITAC

This repository contains a web application that supports the execution of service compositions. The core component of
the web application is the [Activiti](http://activiti.org/) Engine, which supports the execution of service compositions
that are defined as process models compliant with the BPMN 2.0 standard. During the execution of these process models,
the information exchanged (in JSON) between the Activiti Engine and the server allows the web application to dynamically
generate graphical interfaces for the user. These interfaces enable the invocation of the web services that are defined
in the process models.

The web application that is contained in this repository was developed in the context of an ITEA 3 project: [SITAC] (https://itea3.org/project/sitac.html). This project provides a mobile application that allows users to define service
compositions without needing any technical knowledge about BPMN 2.0 or Activiti. These process models can be executed 
as is by the web application.

A video demo of the tool can be found in:

https://www.dropbox.com/s/eoq7soiaeef8go0/EUCalipTool.mp4?dl=0

The tool is available at:

http://personales.upv.es/pedvalar/serviceComposer
