import * as React from 'react';
import {
  Button,
  Modal,
  StyleSheet,
  Switch,
  Text,
  TextInput,
  View,
} from 'react-native';

import type { RequestTokenOptionsType } from 'react-native-incognia';

type RequestTokenOptionsDialogProps = {
  visible: boolean;
  requestTokenOptions: RequestTokenOptionsType;
  onCancel(): void;
  onSave(requestTokenOptions: RequestTokenOptionsType): void;
};

const DEFAULT_TIMEOUT = 12000;
const DEFAULT_REQUEST_TOKEN_MAX_LENGTH = 8000;
const DEFAULT_ENSURE_DATA_COLLECTED = false;
const color = '#4D2C4C';

const onlyDigits = (value: string) => value.replace(/\D/g, '');

const parseOptionalInteger = (value: string): number | undefined => {
  const trimmedValue = value.trim();
  return trimmedValue === '' ? undefined : Number.parseInt(trimmedValue, 10);
};

export const RequestTokenOptionsDialog = ({
  visible,
  requestTokenOptions,
  onCancel,
  onSave,
}: RequestTokenOptionsDialogProps) => {
  const [timeout, setTimeoutValue] = React.useState('');
  const [requestTokenMaxLength, setRequestTokenMaxLength] = React.useState('');
  const [ensureDataCollected, setEnsureDataCollected] = React.useState(false);

  React.useEffect(() => {
    if (!visible) return;

    const androidOptions = requestTokenOptions.androidRequestTokenOptions;
    setTimeoutValue(androidOptions.timeout?.toString() ?? '');
    setRequestTokenMaxLength(
      androidOptions.requestTokenMaxLength?.toString() ?? ''
    );
    setEnsureDataCollected(
      androidOptions.ensureDataCollected ?? DEFAULT_ENSURE_DATA_COLLECTED
    );
  }, [requestTokenOptions, visible]);

  const reset = () => {
    setTimeoutValue(DEFAULT_TIMEOUT.toString());
    setRequestTokenMaxLength(DEFAULT_REQUEST_TOKEN_MAX_LENGTH.toString());
    setEnsureDataCollected(DEFAULT_ENSURE_DATA_COLLECTED);
  };

  const save = () => {
    onSave({
      androidRequestTokenOptions: {
        timeout: parseOptionalInteger(timeout),
        requestTokenMaxLength: parseOptionalInteger(requestTokenMaxLength),
        ensureDataCollected,
      },
    });
  };

  return (
    <Modal
      animationType="fade"
      onRequestClose={onCancel}
      transparent
      visible={visible}
    >
      <View style={styles.backdrop}>
        <View style={styles.dialog}>
          <Text style={styles.title}>RequestTokenOptions</Text>

          <Text style={styles.label}>Timeout</Text>
          <TextInput
            accessibilityLabel="Request token timeout"
            keyboardType="number-pad"
            onChangeText={(value) => setTimeoutValue(onlyDigits(value))}
            placeholder="Default"
            style={styles.input}
            value={timeout}
          />

          <Text style={styles.label}>Max Length</Text>
          <TextInput
            accessibilityLabel="Request token max length"
            keyboardType="number-pad"
            onChangeText={(value) =>
              setRequestTokenMaxLength(onlyDigits(value))
            }
            placeholder="Default"
            style={styles.input}
            value={requestTokenMaxLength}
          />

          <View style={styles.switchRow}>
            <Switch
              accessibilityLabel="Ensure data collected"
              onValueChange={setEnsureDataCollected}
              trackColor={{ false: '#767577', true: '#A879A7' }}
              thumbColor={ensureDataCollected ? color : '#F4F3F4'}
              value={ensureDataCollected}
            />
            <Text style={styles.switchLabel}>Ensure Data Collected</Text>
          </View>

          <View style={styles.actions}>
            <Button color={color} onPress={reset} title="Reset" />
            <Button color={color} onPress={onCancel} title="Cancel" />
            <Button color={color} onPress={save} title="Save" />
          </View>
        </View>
      </View>
    </Modal>
  );
};

const styles = StyleSheet.create({
  backdrop: {
    alignItems: 'center',
    backgroundColor: 'rgba(0, 0, 0, 0.45)',
    flex: 1,
    justifyContent: 'center',
    padding: 24,
  },
  dialog: {
    backgroundColor: '#FFFFFF',
    borderRadius: 8,
    maxWidth: 420,
    padding: 20,
    width: '100%',
  },
  title: {
    color: '#1F1F1F',
    fontSize: 20,
    fontWeight: '600',
    marginBottom: 20,
  },
  label: {
    color: '#333333',
    fontSize: 14,
    marginBottom: 6,
  },
  input: {
    borderColor: '#777777',
    borderRadius: 4,
    borderWidth: 1,
    color: '#1F1F1F',
    marginBottom: 16,
    minHeight: 48,
    paddingHorizontal: 12,
  },
  switchRow: {
    alignItems: 'center',
    flexDirection: 'row',
    marginBottom: 20,
  },
  switchLabel: {
    color: '#1F1F1F',
    flex: 1,
    marginLeft: 10,
  },
  actions: {
    flexDirection: 'row',
    justifyContent: 'space-between',
  },
});
