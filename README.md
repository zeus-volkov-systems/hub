# hub-engine-core

## A Federated Kappa-Style WMS

## clj.1.0.0

*Hub* is a specification and implementation for a federated backend *data* processing system designed for deployment in an orchestrator-managed federated processing environment. The implementation of Hub in this package is provided in Clojure. Hub consists of a very generic processing engine, a controller ring that distributes jobs and waits for returns, a distributed logging system that records tracking state and emitted stateful job state, and various processing endpoints that use different libraries and technologies to accomplish specific functional workflows. Conceptually, Hub can be thought of as a higher level WMS than the ZVS Ephemeral job-task WMS.

Hub is a 'best tool for the job' system, where languages and tools are fungible and decoupled from the system component they implement. The most important thing about hub components is intercommunication, which is managed by a strict and technology-agnostic message ontology. Components can have multiple iterations and multiple versions comprised of vastly different implementation detail.

*hub-engines* are the processing units of hub. Hub-engines consist of a hub-engine-core and one or more hub-engine-libraries.

This repository contains a version of hub-engine-core that is written in clojure, to take advantage of a few features of clojure that make it attractive as a processing core - namely, immutable state, core.async's pub-sub feature, and seamless JVM/GraalVM integration. Hub-engine-cores are decoupled from Hub-engine-libraries - so the only libraries included in this core are test libraries that validate various behaviors of the core itself. In practice, this core would be combined with libraries, and given a unique name in its systemic universe. This combination would serve as a flavor of a hub-engine, that could then be used in the greater context of hub processing.

**To expound on and further illustrate this idea - if we compile hub-engine natively, put it in a container, and manage using an orchestrator, we have a distributed constellation of extremely small and fast processing units that spin up quickly, perform directed processing, compose their own immutable log of actions, and die with a final emission of that log to a distributed messaging system (as a nested log of logs) like Kafka that can do something else with that information.***

## Installation

Download from <https://github.com/zeus-volkov-systems/hub>.

## Usage

Currently we are implementing tests in the core. Once complete we'll provide a build method that compiles this system using GraalVM and packages into a deployable container.

    java -jar hub-0.1.0-standalone.jar

## Examples

Make sure your port 8080 is unused, launch the example using `lein run`, and then submit a job using `http://localhost:8080/?topic=log&content=bar`. You will see the 'messages.txt' updated in the 'resources/logging' directory and the browser will tell you 'success'.

This should begin to tell you how hub expects messages to be broadcast - first on topic and then content. Let's now try to submit a request for some file parsing data. Navigate to `http://localhost:8080/?topic=file&content=1E3SalesRecords` in the browser. Again, success! This time you should see an output.csv file updated within the 'resources/outputs' directory, as well as a log record of the attempt. You can look at the resources/inputs directory to see what input this action was operating on.

Thinking about this a second - we have a single processing unit that uses a stereotyped set of REST parameters, has an internal log (specified here as messages.txt), takes specified inputs (that don't need to be passed in over HTTP or WS), uses some sort of message routing mechanism, and has some set of bound file services.

If you start to dig a bit deeper, you see that the web/server, the message/service, the log/service, utils/service_utils, and utils/map_utils are very small and never need to change. We can replace the services/file/service and utils/file_utils with any other business logic to do any black-box processing, while reusing the rest. All we have to do is create a stereotyped business logic router with registration logic (e.g., file/service), and then register our business logic functions within. Initiated messages all come in through the web request, are  parsed into maps, and topically routed based on package structure, then contents are provided to the function we look up via the subscriber. Then returns actions and data are all precisely timestamped, written to standard locations for final output, and the log of record is published. Cool!

We can start to imagine how this easily reconfigures into a federated processing constellation with no change at the interface level.

Using hub-engine-core means taking the bare bones engine-core, layering self-contained business logic libraries (see utils/file_utils.clj) and services (see the services/file/service.clj), defining a unique metadata identifier file for the collection for federated identification, and adding a container packaging spec.

This hub-engine-core distribution contains an example hub processing job to get us started that demonstrates the basic strategy of REST endpoint acceptance, job handling of a file based etl request, and log-writing/emission. The metadata identification ontology is still being designed but will be a core requirement of hub-engine-core.

Within the larger federated hub, a dedicated job ring will be able to read the index of hub engines and state, distributing data user-side requests asynchronously, and reading response from the nested log stream. The job ring WMS layer is being designed using sanic (python) and nginx and is still in early development.

### Bugs

Hub is in the exciting active development stage of invention and is not ready for production release. If helping to develop hub, please report and track them through github. We are trying to use hub as an opportunity to play with polytech ideas, and welcome all positive contributions.

## License

Copyright © 2021 Zeus Volkov Systems

This program and the accompanying materials are made available under the
terms of the Eclipse Public License 2.0 which is available at
<http://www.eclipse.org/legal/epl-2.0>.

This Source Code may also be made available under the following Secondary
Licenses when the conditions for such availability set forth in the Eclipse
Public License, v. 2.0 are satisfied: GNU General Public License as published by
the Free Software Foundation, either version 2 of the License, or (at your
option) any later version, with the GNU Classpath Exception which is available
at <https://www.gnu.org/software/classpath/license.html>.
