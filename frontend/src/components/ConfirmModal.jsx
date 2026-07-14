import { Button, Group, Modal, Text } from '@mantine/core';

export default function ConfirmModal({
  opened,
  title,
  message,
  confirmLabel = 'Eliminar',
  confirmColor = 'red',
  loading = false,
  onConfirm,
  onCancel,
}) {
  return (
    <Modal opened={opened} onClose={onCancel} title={title} centered size="sm">
      <Text size="sm">{message}</Text>
      <Group justify="flex-end" mt="lg">
        <Button variant="default" onClick={onCancel}>
          Cancelar
        </Button>
        <Button color={confirmColor} loading={loading} onClick={onConfirm}>
          {confirmLabel}
        </Button>
      </Group>
    </Modal>
  );
}
