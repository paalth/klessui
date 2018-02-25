SHELL := /bin/bash
MAKEFLAGS += --no-builtin-rules

# TODO: do something smarter than sed...

all:
	build-tools/build.sh

image:
	build-tools/create-image.sh

deploy-ui:
	deploy/deploy-ui.sh

undeploy-ui:
	deploy/undeploy-ui.sh
