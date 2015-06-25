package code.config;

import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import code.project.ProjectClazz;
import code.project.ProjectCodeFile;
import code.project.ProjectPackage;

public class ConfigPackage extends ConfigCodeFile {

    private ProjectPackage projectPackage;

    private ProjectPackage testPackage;

    private ConfigPackage parentPackage;

    private List<ConfigPackage> packages;

    private List<ConfigClazz> clazzes;

    private List<ConfigCodeFile> components;

    public ConfigPackage(ProjectPackage projectPackage, ConfigPackage parentPackage, ProjectPackage testPackage) {
        this.projectPackage = projectPackage;
        this.parentPackage = parentPackage;
        this.testPackage = testPackage;
        this.name = this.projectPackage.getName();
        this.packages = new ArrayList<ConfigPackage>();
        this.clazzes = new ArrayList<ConfigClazz>();
        this.components = new ArrayList<ConfigCodeFile>();
        this.build();
    }

    private void build() {
        for (ProjectPackage projectPackage : this.projectPackage.getPackages()) {
            this.packages.add(new ConfigPackage(projectPackage, this, this.testPackage.getPackage(projectPackage.getName())));
        }
        for (ProjectClazz clazz : this.projectPackage.getClazzes()) {
            this.clazzes.add(new ConfigClazz(clazz, this, this.testPackage.getClazz(clazz.getName())));
        }
        this.components.addAll(packages);
        this.components.addAll(clazzes);
    }

    public String getName() {
        return name;
    }

    public ConfigPackage getParentPackage() {
        return parentPackage;
    }

    @SuppressWarnings("unchecked")
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("name", this.name);
        JSONArray clazzes = new JSONArray();
        for (ConfigClazz clazz : this.clazzes) {
            clazzes.add(clazz.toJson());
        }
        json.put("classes", clazzes);
        JSONArray packages = new JSONArray();
        for (ConfigPackage configPackage : this.packages) {
            packages.add(configPackage.toJson());
        }
        json.put("packages", packages);
        return json;
    }
}
