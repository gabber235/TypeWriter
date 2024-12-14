import React from 'react';
import { Icon } from '@iconify/react';

interface ActionButtonProps {
  button: 'capture' | 'dynamic-variable' | 'remove';
  onClick: () => void;
  tooltip?: string;
  color?: string;
}

const ActionButton: React.FC<ActionButtonProps> = ({ button, onClick, tooltip = '', color }) => {
  let iconName = '';
  let backgroundColor = color || '';

  switch (button) {
    case 'capture':
      iconName = 'material-symbols:photo-camera';
      backgroundColor = backgroundColor || '#2196f3';
      break;
    case 'dynamic-variable':
      iconName = 'ph:brackets-curly-bold';
      backgroundColor = backgroundColor || '#4caf50';
      break;
    case 'remove':
      iconName = 'fa6-solid:xmark';
      backgroundColor = backgroundColor || '#f44336';
      break;
    default:
      iconName = 'bx:bxs-error';
      backgroundColor = backgroundColor || '#eb4034';
  }

  const iconStyle: React.CSSProperties = {
    fontSize: '24px',
    color: '#FFFFFF',
    padding: '4px',
    backgroundColor: backgroundColor,
    borderRadius: '4px',
    verticalAlign: 'middle',
    minWidth: '25px',
  };

  return (
    <Icon icon={iconName} style={iconStyle} />
  );
};

export default ActionButton;