RoboBee - Simple Server Control
===============================

# Introduction

![RoboBee overview.](https://anrisoftware.com/projects/projects/sscontrol-osgi-docu/repository/revisions/master/raw/images/sscontrol-overview_en.svg)

## Idea

The idea to create a new server configuration tool was born out of a simple concideration,
the configuration of each new server is always the same for the same stack, and the stack
is always similar, i.e. web server, database server, email server, DNS server.
So, I asked myself, what if I could just tell the computer to install and configure me the stack
on the server, without for me to actually have any knowledge how to configure it.
The user should be able to tell the computer to just install and setup, for example, a web-server
without to have the actual knowledge how to do it. All the knowledge how to 
install and configure the server should be contained in the application that is 
installing and configures the server.

## Expert Knowledge

RoboBee will contain expert knowledge to install and configure the server
with the needed services and applications. The expert knowledge will contain 
best practices in regards to security and it will updated regularly to
fix bugs and to react to new security issues.
To ensure a constant flow of updated expert knowledge, the [OSGi technology](https://www.osgi.org)
will be used for RoboBee. OSGi container allows for seamlessly updates of
the modules that contain the expert knowledge from a central repository.

## OSGi Container

There are multiple OSGi container implementations (Apache Felix, Equinox OSGi, Knopflerfish, etc.)
and RoboBee will be able
to run on those, but also it will provide its own container, [Karaf](http://karaf.apache.org).
Based on the Karaf container, RoboBee can be installed on a GNU/Linux server
just as any other application and run as a service in the background.
The communication between RoboBee and the user is done via a SSH connection and the
OSGi console.

## Domain Specific Language

The user is expected to write simple scripts in a domain specific language (DSL)
based on Groovy that communicate the wishes of the user to RoboBee. A syntax
must be followed that groups the server services into categories, like web server,
DNS server, mail server, etc. and also have application categories like Wordpress,
Drupal, Postfix, MySQL, etc. The user is assumed to have only very basic knowledge 
of those services and applications, for example, the user should know that the MySQL database
server have an administration user, provides databases and have users that have
permissions to create tables in those databases. But this kind of knowledge
is not expert knowledge and is expected from the user to have. Expert knowledge in this
case would be how to install the MySQL server, configure the server, create
databases and users on the server and grant the users permissions to those databases.

# Vision of the Solution

## Vision Statement

The RoboBee (aka Simple Server Control) is about to fully configure a 
server by defined profiles and script files using a domain specific language
(DSL). The goal of the project is to follow high level user specifications
to install and configure specific services on the server. RoboBee
will follow the ["Ask, don't tell"](https://pragprog.com/articles/tell-dont-ask), 
the ["Convention over configuration"](https://en.wikipedia.org/wiki/Convention_over_configuration)
and the ["Principle of least knowledge"](https://en.wikipedia.org/wiki/Law_of_Demeter) 
philosophies and principles.

**"Ask, don't tell",**

the user is expected to ask the different RoboBee services to install and 
configure the server. The services will install and 
configure the server according to the wishes of the user and will not expect
from the user to know how to do it. 

**"Convention over configuration",**

the user is expected to follow the convention set from RoboBee to reduce
the amount of needed configuration. Basically, to follow configuration
file name conventions, and to follow protected configuration of the server.

**"Principle of least knowledge",**

the user is not expected to have any expert knowledge of how to install or 
configure the server, all expoert knowledge is contained in RoboBee.

## Major Features

### F1-a. Install Server Services,

installs server services on the server. Server services are usually
DNS server to resolve DNS names (Bind, MaraDns), Httpd server to serve websites (Apache, Tomcat),
proxy server for caching (Nginx, Squid), firewall to block harmful packages, MTA (Postfix, Exim, qmail),
MDA (Dovecot, Courier), etc.

### F1-b. Configures Server Services,

configures server services on the server according to the whishes to the user.
The manually edited configuration will be preserved as far as possible.

### F2-a. Installs Applications,

installs server applications that are run on the server. Applications 
are usually run on already installed and configured server services like Apache or Tomcat.
Those are, for example, Wordpress, Drupal, Squirrelmail, Roundcube, Redmine, JIRA, etc.

### F2-b. Installs Applications,

configures the applications on the server according to the whishes to the user.
The manually edited configuration will be preserved as far as possible.

### F3. Profiles,

the system dependent configuration is stored in profiles and can be selected
without the need to modify the script files. Different systems have different
commands to configure the server (SysVInit, upstart, systemd, etc.) and have different
paths of the commands and configuration files.

## Dependencies

### D1. Java,

the underlying technology of the project.

### D2. OSGi,

the underlying technology of the project. RoboBee is seperate the different
services in bundles that can be started and updated at run-time.

### D3. Groovy,

the framework to parse the domain specific language of the script files.

### D4. StringTemplate,

the framework to create service configuration files.

## Related Projects

### Puppet,

is a configuration management for systems automation and 
uses Json data structures in manifests files, but also uses 
a declarative domain specific language (DSL) based on Ruby. It is developed
by [Puppet Labs](https://puppetlabs.com)

### Chef,

> "[is a] configuration management tool written in Ruby and Erlang. It uses a 
pure-Ruby, domain-specific language (DSL) for writing system configuration 
"recipes". Chef is used to streamline the task of configuring and maintaining a 
company's servers, and can integrate with cloud-based platforms such as 
Internap, Amazon EC2, Google Cloud Platform, OpenStack, SoftLayer, Microsoft 
Azure and Rackspace to automatically provision and configure new machines. Chef 
contains solutions for both small and large scale systems, with features and 
pricing for the respective ranges."

https://en.wikipedia.org/wiki/Chef_(software)


### CFEngine,

> "is an open source configuration management system, written by Mark 
Burgess. Its primary function is to provide automated configuration and 
maintenance of large-scale computer systems, including the unified management of 
servers, desktops, consumer and industrial devices, embedded networked devices, 
mobile smartphones, and tablet computers."

https://en.wikipedia.org/wiki/CFEngine


### Salt,

> "SaltStack platform or Salt is a Python-based open source configuration 
management software and remote execution engine. Supporting the "Infrastructure 
as Code" approach to deployment and cloud management."

https://en.wikipedia.org/wiki/Salt_(software)

### Ansible,

> "a free-software platform for configuring and managing computers, combines 
multi-node software deployment, ad hoc task execution, and configuration 
management. It manages nodes (which must have Python 2.4 or later installed 
on them) over SSH or over PowerShell. Modules work over JSON and standard 
output and can be written in any programming language. The system uses YAML to 
express reusable descriptions of systems."

https://en.wikipedia.org/wiki/Ansible_(software)
