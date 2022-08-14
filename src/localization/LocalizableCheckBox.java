package localization;

import javax.swing.JCheckBox;

public class LocalizableCheckBox extends JCheckBox implements Localizable {
	private Localizer module;
	private String key;
	
	public LocalizableCheckBox(String key, Localizer module) {
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
		this.setText(module.getTranslation(key));		
	}
}
