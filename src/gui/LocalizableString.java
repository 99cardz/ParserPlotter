package gui;

import localization.Localizable;
import localization.Localizer;

public class LocalizableString implements Localizable {
	private Localizer module;
	private String key;
	private String s;
	
	public LocalizableString(String key, Localizer module) {
		setTranslations(key, module);
	}

	public void setTranslations(String key, Localizer module) {
		this.module = module;
		this.key = key;
		this.module.register(this);
	}
	
	public void update() {
		this.s = module.getTranslation(key);
	}
	
	public String get() {
		return this.s;
	}
}
