import firebase from 'react-native-firebase';
import InLocoEngage from 'react-native-incognia';

export default async (message) => {
    if(InLocoEngage.isInLocoEngageMessage(message)) {
        InLocoEngage.presentNotification(message);
    }
    return Promise.resolve();
}