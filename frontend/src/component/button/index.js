import {createElement} from 'react';
import styles from './index.css';

export default (props) => {
  return <button className={styles.button} {...props}/>;
};

export const SubmitBtn = (props) => {
  return <button className={styles.submitBtn} {...props}/>;
};
