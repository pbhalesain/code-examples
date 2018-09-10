package io.reflectoring;

import java.util.List;

import au.com.dius.pact.model.Interaction;
import au.com.dius.pact.model.ProviderState;
import au.com.dius.pact.provider.ConsumerInfo;
import au.com.dius.pact.provider.ProviderInfo;
import au.com.dius.pact.provider.ProviderVerifier;
import au.com.dius.pact.provider.junit.target.AmqpTarget;
import org.jetbrains.annotations.NotNull;

public class CustomAmqpTarget extends AmqpTarget {

	public CustomAmqpTarget(List<String> packagesToScan) {
		super(packagesToScan);
	}

	@NotNull
	@Override
	protected ProviderVerifier setupVerifier(Interaction interaction, ProviderInfo provider, ConsumerInfo consumer) {
		ProviderVerifier verifier = new CustomProviderVerifier(getPackagesToScan());
		setupReporters(verifier, provider.getName(), interaction.getDescription());
		verifier.initialiseReporters(provider);
		verifier.reportVerificationForConsumer(consumer, provider);

		if (!interaction.getProviderStates().isEmpty()) {
			for (ProviderState state : interaction.getProviderStates()) {
				verifier.reportStateForInteraction(state.getName(), provider, consumer, true);
			}
		}
		verifier.reportInteractionDescription(interaction);
		return verifier;
	}
}
