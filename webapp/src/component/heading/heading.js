import {createElement} from 'react';
import classNames from 'classnames';
import styles from './heading.css';

const Heading = ({children, kind}) => (
  <h1
    className={classNames(
      styles.heading,
      styles[kind]
    )}
  >{children}</h1>
);

export default Heading;
