package localization;

import javax.swing.border.TitledBorder;

public class LocalizableTitledBorder extends TitledBorder implements Localizable {
	private Localizer module;
	private String key;
	
	public LocalizableTitledBorder(String key, Localizer module) {
		super(key);
		setTranslations(key, module);
	}

	@Override
	public void setTranslations(String key, Localizer module) {
		this.module = module;
		this.key = key;
		this.module.register(this);
	}
	
	@Override
	public void update() {
		this.setTitle(module.getTranslation(key));		
	}
}
