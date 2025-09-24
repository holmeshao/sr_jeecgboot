Jeecg NiFi Plugins

This module provides a standard Apache NiFi bundle layout:

- jeecg-nifi-processors (jar): custom processors
- jeecg-nifi-nar (nar): bundles the processors into a deployable NAR

Build

```
mvn -pl jeecg-boot/jeecg-boot-module/jeecg-boot-module-nifi-plugins -am clean package -DskipTests
```

Output NAR: jeecg-nifi-nar/target/jeecg-nifi-nar-*.nar

Deploy options

1) Volume mount into NiFi container (docker-compose):
- Copy NAR to host path /opt/docker/nifi/extensions
- Restart NiFi container

2) Build a custom NiFi image:
- Use provided Dockerfile.nifi:

```
cd jeecg-boot/jeecg-boot-module/jeecg-boot-module-nifi-plugins
mvn -am -DskipTests clean package
docker build -f Dockerfile.nifi -t jeecg-nifi:1.25.0 .
```

- Update docker-compose to use `image: jeecg-nifi:1.25.0`

After deploy, search for LaborTeamProcessor in the NiFi UI.


