package space.kovo.nocnyprud2.backend.singletons

import space.kovo.nocnyprud2.backend.entities.yamlValues.WizardEntity

object Values {
    // cannot be loaded directly, because for YAML file it needs to wait for application context to be ready
    // fillup with data is performed in space.kovo.nocnyprud2.backend.inits.ValuesInit
    var wizard: WizardEntity = WizardEntity(emptyList(), "")
}
