# Logging constants
BLUE := \033[1;34m\xe2\x9e\xa1\xef\xb8\x8f  #
YELLOW := \033[1;33m\xe2\x9a\xa0\xef\xb8\x8f  #
GREEN := \033[1;32m\xe2\x9c\x85  #
RED := \033[1;31m\xf0\x9f\x9a\xab  #
NC := \033[0m

# Gradle
.PHONY: update-dependencies
update-dependencies: ## Updates Gradle Lockfiles after dependencies are added or changed.
	@echo "${BLUE}Updating dependencies...${NC}"
	@cd example/android && ./gradlew dependencies --write-locks
	@cd example/android && ./gradlew :reactnativeincognia:dependencies --write-locks
	@cd example/android && ./gradlew :app:dependencies --write-locks
	@echo "${GREEN}Gradle Lockfiles updated!${NC}\n"

# Help
help: ## View this help dialog.
	@IFS=$$'\n' ; \
	help_lines=(`fgrep -h "##" $(MAKEFILE_LIST) | fgrep -v fgrep | sed -e 's/\\$$//' | sed -e 's/##/:/'`); \
	printf "%-30s %s\n" "target" "help" ; \
	printf "%-30s %s\n" "------" "----" ; \
	for help_line in $${help_lines[@]}; do \
		IFS=$$':' ; \
		help_split=($$help_line) ; \
		help_command=`echo $${help_split[0]} | sed -e 's/^ *//' -e 's/ *$$//'` ; \
		help_info=`echo $${help_split[2]} | sed -e 's/^ *//' -e 's/ *$$//'` ; \
		printf '\033[36m'; \
		printf "%-30s %s" $$help_command ; \
		printf '\033[0m'; \
		printf "%s\n" $$help_info; \
	done