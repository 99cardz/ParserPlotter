package localization;

import javax.swing.JLabel;

public class LocalizableLabel extends JLabel implements Localizable {
	private Localizer module;
	private String key;
	
	public LocalizableLabel(String key, Localizer module) {
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
		this.setText(module.getTranslation(key));		
	}
}
