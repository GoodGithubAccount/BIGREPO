import {render, screen} from '@testing-library/react';
import {describe, expect, it} from 'vitest';
import App from './App';

describe(App.name, () => {
	it('should render', () => {
		render(<App/>);
		expect(screen.getByRole('heading', {level: 2, name: /SHOPshop/i})).toBeInTheDocument();
		expect(screen.getByRole('heading', {level: 4, name: /SHOPshop/i})).toBeInTheDocument();
	});
});
